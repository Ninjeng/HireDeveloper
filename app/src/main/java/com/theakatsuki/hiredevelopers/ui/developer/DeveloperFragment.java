package com.theakatsuki.hiredevelopers.ui.developer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.theakatsuki.hiredevelopers.Activity.JobsActivity;

import com.theakatsuki.hiredevelopers.Model.User;
import com.theakatsuki.hiredevelopers.R;




public class DeveloperFragment extends Fragment {

    RecyclerView  mResultList;
    EditText mSearchField;
    ImageButton mSearchBtn;
    LinearLayout webLinear,designLinear,writingLinear,salesLinear,mobileLinear,dataLinear;
    DatabaseReference mUserDatabase;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_developer, container, false);
        webLinear = view.findViewById(R.id.webLinear);
        designLinear = view.findViewById(R.id.designLinear);
        writingLinear = view.findViewById(R.id.writingLinear);
        salesLinear = view.findViewById(R.id.salesLinear);
        mobileLinear = view.findViewById(R.id.mobileLinear);
        dataLinear = view.findViewById(R.id.dataLinear);
        webLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Website");
                startActivity(intent);
            }
        });
        designLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Design");
                startActivity(intent);
            }
        });
        writingLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Content");
                startActivity(intent);
            }
        });
        salesLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Marketing");
                startActivity(intent);
            }
        });
        mobileLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Mobile");
                startActivity(intent);
            }
        });
        dataLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JobsActivity.class);
                intent.putExtra("category","Data");
                startActivity(intent);
            }
        });

        mUserDatabase= FirebaseDatabase.getInstance().getReference("Users");
        mSearchField=view.findViewById(R.id.search_field);
        mSearchBtn=view.findViewById(R.id.search_btn);

        mResultList = (RecyclerView) view.findViewById(R.id.result_list);
        mResultList.setHasFixedSize(false);
        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(mSearchField.getText().toString())){
                    mSearchField.setError("Please enter Value");
                    mSearchField.requestFocus();
                }
                else {
                    String searchText = mSearchField.getText().toString();

                    firebaseUserSearch(searchText);
                }
            }
        });


        return view;
    }


    private void firebaseUserSearch(String searchText) {


        Query firebaseSearchQuery = mUserDatabase.orderByChild("fullname").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<User, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User,UsersViewHolder>(

                User.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, User model, int position) {


                viewHolder.setDetails(getContext(), model.getFullname(), model.getPhoneNumber(), model.getProfileImage());

            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
        Toast.makeText(getContext(), "Check complete", Toast.LENGTH_LONG).show();

    }



    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String fullname, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(fullname);
            user_status.setText(userStatus);
            if(userImage.equals("Default"))
            {
                Glide.with(ctx).load(R.drawable.male).into(user_image);

            }
            else{
                Glide.with(ctx).load(userImage).into(user_image);
            }



        }

    }

}
