package com.theakatsuki.hiredevelopers.ui.notifications;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Adapter.NotificationAdapter;
import com.theakatsuki.hiredevelopers.Model.Hire;
import com.theakatsuki.hiredevelopers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    List<Hire> hireList;
    FirebaseUser firebaseUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.recycler_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        showNotification();

        return root;
    }

    private void showNotification() {
        hireList = new ArrayList<>();
        hireList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Hire hire = snapshot.getValue(Hire.class);
                    if(hire.getHiringUserId().equals(firebaseUser.getUid()))
                    {
                        hireList.add(hire);
                    }
                }
                Collections.reverse(hireList);
                NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(),hireList);
                recyclerView.setAdapter(notificationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}