package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.theakatsuki.hiredevelopers.R;
import com.theakatsuki.hiredevelopers.Notification.createChannel;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PasswordResetActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnResetPassword;
    private Button btnBack;
    private FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        getSupportActionBar().setTitle("Reset password");

        edtEmail = (EditText) findViewById(R.id.edt_reset_email);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);

        mAuth=FirebaseAuth.getInstance();
        notificationManagerCompat=NotificationManagerCompat.from(this);
        createChannel createChannel=new createChannel(this);
        createChannel createChannel1;

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Please enter Email Address");
                    edtEmail.requestFocus();
                }

                else if(!email.matches(emailPattern)){
                    edtEmail.setError("Email format incorrect");
                    edtEmail.requestFocus();
                }
                else {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PasswordResetActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(PasswordResetActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        DisplayNotification1();
                                    } else {
                                        Toast.makeText(PasswordResetActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    final int min=100000;
    final int max=1000000;
    final int r= ThreadLocalRandom.current().nextInt(min,max);
    int id=1;

    private void DisplayNotification1() {

        Notification notification=new NotificationCompat.Builder(this,createChannel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("Email Code :"+r )
                .setContentText("Your Email is " +edtEmail.getText().toString())
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(id,notification);
        id++;
    }

}