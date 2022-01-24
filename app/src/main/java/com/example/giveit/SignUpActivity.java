package com.example.giveit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    EditText fullname,phone,email,password;
    String em,ph,name,pass;
    Button signup,login;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(binding.getRoot());
        setContentView(R.layout.activity_sign_up);

        fullname = findViewById(R.id.fullnameEt);
        phone = findViewById(R.id.phoneEt);
        email = findViewById(R.id.emailEt);
        password = findViewById(R.id.passwordEt);
        signup = findViewById(R.id.signupBtnEt);
        login = findViewById(R.id.loginBtnEt);

        //initialise firebase auth
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });


    }



    private void validateData(){
        em = email.getText().toString().trim();
        pass = password.getText().toString().trim();
        name = fullname.getText().toString().trim();
        ph = phone.getText().toString().trim();


        //validation
        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            //email format is invalid, don't proceed further
            email.setError("Invalid email format");
        }
        if(TextUtils.isEmpty(pass)){
            password.setError("Enter a password");
        }
        if(password.length()<6){
            password.setError("password must be at least 6 characters");
        }
        if(TextUtils.isEmpty(name)){
            fullname.setError("fullname is required");
        }
        else{
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {

        auth.createUserWithEmailAndPassword(em,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("users");
                            FirebaseUser fUser = auth.getCurrentUser();
                            User user = new User(name,ph);

                            databaseReference.child(fUser.getUid().toString()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SignUpActivity.this, "signup failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}