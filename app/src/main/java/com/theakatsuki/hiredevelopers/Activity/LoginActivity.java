package com.theakatsuki.hiredevelopers.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theakatsuki.hiredevelopers.R;


public class LoginActivity extends AppCompatActivity {
    EditText etusername, etpassword;
    Button btnLogin;
    TextView btnCreateNewAccount;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    TextView resetPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private CheckBox showPassword;
    ProgressDialog loadingBar;
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(firebaseUser!=null)
//        {
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            finish();
//
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressbar);
        etusername = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        etusername.setText("koc@gmail.com");
        etpassword.setText("password");
        btnLogin = findViewById(R.id.btnLogin);
        resetPassword=findViewById(R.id.forgot_password_text);
        String[] arraySpinner = new String[] {
                "User","Developer"
        };
        loadingBar=new ProgressDialog(this);

        showPassword=findViewById(R.id.show_password);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                else{
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,PasswordResetActivity.class);
                startActivity(intent);
            }
        });

        btnCreateNewAccount = findViewById(R.id.createAccount);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etusername.getText().toString();
                String password = etpassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    etusername.setError("Email Not entered");
                    etusername.requestFocus();
                 }
                else if(TextUtils.isEmpty(password)){
                    etpassword.setError("Password not Entered");
                    etpassword.requestFocus();
                 }
                else if(!email.matches(emailPattern)){
                    etusername.setError("Email format incorrect");
                    etusername.requestFocus();
                }

                else if(etpassword.length()<8){
                    etpassword.setError("Password length at least 8 characters");
                    etpassword.requestFocus();
                }

                else {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait.. "+ "\n"+"Checking Credential...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    loginUser(email,password);

                }
            }
        });

        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UserOptionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            loadingBar.dismiss();
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(500);
                            loadingBar.dismiss();
                        }
                    }
                });
    }


}