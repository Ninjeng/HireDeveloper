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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theakatsuki.hiredevelopers.Activity.CommentActivity;
import com.theakatsuki.hiredevelopers.Activity.ProfileActivity;
import com.theakatsuki.hiredevelopers.Model.Events;
import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private Context myContext;
    private List<Events> events;
    private String uid;
    FirebaseUser firebaseUser;

    public ProfileAdapter(Context myContext, List<Events> events, String uid) {
        this.myContext = myContext;
        this.events = events;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.event_display,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

       final Events event = events.get(position);
       holder.content.setText(event.getContent());
       if(event.getEventImage().equals("Blank"))
       {
           holder.imageView.setVisibility(View.GONE);
       }
       else {
           holder.imageView.setVisibility(View.VISIBLE);
           Glide.with(myContext.getApplicationContext()).load(event.getEventImage()).into(holder.imageView);

           holder.userProfileImage.setImageResource(R.mipmap.ic_launcher);

       }
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(event.getUserId());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               User user = dataSnapshot.getValue(User.class);
               holder.country.setText(user.getCountry());
               holder.fullName.setText(user.getFullname());

               if(user.getProfileImage().equals("Default"))
               {
                   holder.userProfileImage.setImageResource(R.drawable.male);

               }
               else
               {
                   Glide.with(myContext.getApplicationContext()).load(user.getProfileImage()).into(holder.userProfileImage);
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
        CheckLike(event.getPostId(),holder.like);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getTag().equals("Like"))
                {
                    FirebaseDatabase.getInstance().getReference("Activities").child(event.getPostId()).child("Like").child(firebaseUser.getUid()).setValue(true);

                }
                else if (holder.like.getTag().equals("Liked"))
                {
                    FirebaseDatabase.getInstance().getReference("Activities").child(event.getPostId()).child("Like").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContext, CommentActivity.class);
                intent.putExtra("PostId",event.getPostId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userProfileImage;

        ImageView imageView;
        TextView fullName, content, followers, country;
        ImageView like , comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage1);
            imageView = itemView.findViewById(R.id.ContentImage1);
            fullName = itemView.findViewById(R.id.proUsername1);
            country = itemView.findViewById(R.id.countryName1);
            content = itemView.findViewById(R.id.eventContent1);
            like = itemView.findViewById(R.id.btnLike1);
            comment = itemView.findViewById(R.id.btnComment1);
        }
    }

    private void CheckLike(final String eventId, final ImageView imageView  )
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities").child(eventId).child("Like");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_baseline_blue0thumb_up_24);
                    imageView.setTag("Liked");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_baseline_thumb_up_24);
                    imageView.setTag("Like");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
