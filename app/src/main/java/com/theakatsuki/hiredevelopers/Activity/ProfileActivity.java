package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Adapter.HomeAdapter;
import com.theakatsuki.hiredevelopers.Adapter.JobAdapter;
import com.theakatsuki.hiredevelopers.Adapter.ProfileAdapter;
import com.theakatsuki.hiredevelopers.AllFunctions;
import com.theakatsuki.hiredevelopers.Model.Events;
import com.theakatsuki.hiredevelopers.Model.Job;
import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {



    CircleImageView circleImageView;
    TextView name, post, followers,following,bio,country,phoneNumber,skills,work;
    ImageView home, job, about;
    RecyclerView recyclerView;
    String userId,jbId="";
    List<Events> events;
    List<Job> jobs;
    FirebaseUser firebaseUser;
    ProfileAdapter profileAdapter;
    Button btnMessage, btnFollow;
    LinearLayout linearLayout;
    RelativeLayout aboutLayout;
    Button btnEdit,btnHire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        btnEdit= findViewById(R.id.btnEdit);
        circleImageView= findViewById(R.id.proImage);
        name= findViewById(R.id.proName);
        post= findViewById(R.id.proPost);
        followers= findViewById(R.id.proFollowers);
        following= findViewById(R.id.proFollowing);
        bio= findViewById(R.id.aboutBio);
        phoneNumber= findViewById(R.id.aboutPhoneNumber);
        work= findViewById(R.id.aboutWork);
        skills= findViewById(R.id.aboutSkills);
        country= findViewById(R.id.aboutCountry);
        home= findViewById(R.id.proHome);
        job= findViewById(R.id.proWork);
        about= findViewById(R.id.proAbout);
        recyclerView= findViewById(R.id.proHomeRecyclerView);
        linearLayout = findViewById(R.id.linearL);
        aboutLayout = findViewById(R.id.aboutPage);
        btnFollow = findViewById(R.id.proBtnFollow);
        btnMessage = findViewById(R.id.proBtnMessage);
        btnHire = findViewById(R.id.btnHire);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent intent = getIntent();
        userId = intent.getStringExtra("UID");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        readProfile(userId);
        readFollowers(userId);
        readFollowing(userId);
        jobs = new ArrayList<>();
        events = new ArrayList<>();
        home.setBackgroundColor(getResources().getColor(R.color.clickColor));
        readEvents(userId);
        if (firebaseUser.getUid().equals(userId))
        {
            btnEdit.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

        else
        {
            btnEdit.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllFunctions allFunctions = new AllFunctions();
                if (btnFollow.getText().equals("Follow"))
                {
                   allFunctions.FollowUser(firebaseUser.getUid(),userId);
                }
                else
                {
                    allFunctions.RemoveFollow(firebaseUser.getUid(),userId);
                }
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });
        aboutLayout.setVisibility(View.GONE);

        checkFollowing(userId,btnFollow);
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                home.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                job.setBackgroundColor(getResources().getColor(R.color.clickColor));
                about.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                readJobs(userId);
                aboutLayout.setVisibility(View.GONE);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                home.setBackgroundColor(getResources().getColor(R.color.clickColor));
                job.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                about.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                readEvents(userId);
                aboutLayout.setVisibility(View.GONE);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                job.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                about.setBackgroundColor(getResources().getColor(R.color.clickColor));
                recyclerView.setVisibility(View.GONE);
                aboutLayout.setVisibility(View.VISIBLE);
            }
        });
        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),HireActivity.class);
                intent1.putExtra("userId",userId);
                startActivity(intent1);
            }
        });

    }
    public void readProfile(final String userId)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getProfileImage().equals("Default"))
                    circleImageView.setImageResource(R.drawable.male);
                else
                    Glide.with(getApplicationContext()).load(user.getProfileImage()).into(circleImageView);
                name.setText(user.getFullname());
                work.setText(user.getWork());
                country.setText(user.getCountry());
                bio.setText(user.getBio());
                phoneNumber.setText(user.getPhoneNumber());
                List<String> programming= user.getProgramming();
                String skillText="";
                for(String program:programming)
                {
                    skillText=program+"\n"+skillText;

                }
                skills.setText(skillText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readFollowing (final String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(userId).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readFollowers (final String userId)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(userId).child("Followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void readEvents (final String userId)
    {
        events.clear();
        jobs.clear();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                {
                    Events event = dataSnapshot1.getValue(Events.class);
                    if(event.getUserId().equals(userId))
                    {
                        events.add(event);
                    }

                }
                Collections.reverse(events);
                post.setText(events.size()+"");
                profileAdapter = new ProfileAdapter(getApplicationContext(),events,firebaseUser.getUid());
                recyclerView.setAdapter(profileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readJobs (final String userId)
    {

        events.clear();
        jobs.clear();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jobs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren())
                {
                    Job job = dataSnapshot1.getValue(Job.class);
                    if(job.getUserId().equals(userId))
                    {
                        jobs.add(job);
                    }

                }
                String category = "null";
                Collections.reverse(jobs);
                JobAdapter jobAdapter = new JobAdapter(getApplicationContext(),jobs,category);
                recyclerView.setAdapter(jobAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void checkFollowing(final String userID, final Button button)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid()).child("Following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userID).exists())
                {
                    button.setText("Following");
                }
                else {
                    button.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}