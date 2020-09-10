package com.theakatsuki.hiredevelopers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Activity.NotificationActivity;
import com.theakatsuki.hiredevelopers.Model.Bid;
import com.theakatsuki.hiredevelopers.Model.Hire;
import com.theakatsuki.hiredevelopers.Model.Notification;
import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private Context context;
    private List<Hire> hireList;

    FirebaseUser firebaseUser;
    public NotificationAdapter(Context context, List<Hire> hireList){
        this.context = context;
        this.hireList = hireList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bid_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Hire hire  = hireList.get(position);
        if(hire.getInvitation().equals(true))
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(hire.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.username.setText(user.getFullname());
                    holder.price.setText(user.getFullname()+" has send a hire invitation to you"+"\n"
                            +"Click for more details");
                    if(user.getProfileImage().equals("Default"))
                    {
                        Glide.with(context).load(R.drawable.male).into(holder.profile);
                    }
                    else
                    {
                        Glide.with(context).load(user.getProfileImage()).into(holder.profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (hire.getRejection().equals("Yes"))
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(hire.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.username.setText(user.getFullname());
                    holder.price.setText(hire.getDescription()+"\n"
                    +"Click to see more");
                    if(user.getProfileImage().equals("Default"))
                    {
                        Glide.with(context).load(R.drawable.male).into(holder.profile);
                    }
                    else
                    {
                        Glide.with(context).load(user.getProfileImage()).into(holder.profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (hire.getRejection().equals("No")|| hire.getInvitation().equals(false))
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(hire.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    holder.username.setText(user.getFullname());
                    holder.price.setText(hire.getDescription()+"\n"
                            +"Click to see more");
                    if(user.getProfileImage().equals("Default"))
                    {
                        Glide.with(context).load(R.drawable.male).into(holder.profile);
                    }
                    else
                    {
                        Glide.with(context).load(user.getProfileImage()).into(holder.profile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        holder.description.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotificationActivity.class);
                intent.putExtra("hireId",hire.getHireId());
                intent.putExtra("hiringUserId",hire.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hireList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username,price,description;
        public ImageView profile;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.name_text);
            price=itemView.findViewById(R.id.price_text);
            description=itemView.findViewById(R.id.description_text);
            profile=itemView.findViewById(R.id.bid_profile_image);


        }
    }

}
