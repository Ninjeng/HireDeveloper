package com.theakatsuki.hiredevelopers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.theakatsuki.hiredevelopers.Activity.MessageActivity;
import com.theakatsuki.hiredevelopers.Activity.ProfileActivity;
import com.theakatsuki.hiredevelopers.Model.Chat;
import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private boolean isChat;

    public DeveloperAdapter(Context context, List<User> users, boolean isChat) {
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.username.setText(user.getFullname());
        holder.lastmessage.setVisibility(View.GONE);
        if(user.getProfileImage().equals("Default"))
        {
            holder.profile.setImageResource(R.drawable.male);
        }
        else {
            Glide.with(context.getApplicationContext()).load(user.getProfileImage()).into(holder.profile);
        }
        if(isChat)
        {
            if(user.getStatus().equals("online"))
            {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.img_on.setVisibility(View.INVISIBLE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.img_on.setVisibility(View.INVISIBLE);
            holder.img_off.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("UID",user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username,lastmessage;
        public CircleImageView profile;
        public CircleImageView img_off;
        public CircleImageView img_on;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.proUsername);
            profile=itemView.findViewById(R.id.userProfileImage);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
            lastmessage=itemView.findViewById(R.id.lastMessage);

        }
    }


}
