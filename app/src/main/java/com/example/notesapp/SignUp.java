package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "sign in";
    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();


        binding.signUpBtnCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nEmail = binding.signEmailTIE.getText().toString();
                String nPass = binding.signPassTIE.getText().toString();
                Log.d(TAG, "onClick: signIn clciked");
                if (nEmail.isEmpty() || nPass.isEmpty()) {
                    Toast.makeText(SignUp.this, "Enter your email and password", Toast.LENGTH_SHORT).show();
                } else if (nPass.length() <8 ) {
                    Toast.makeText(SignUp.this, "Password length must be greater than 8 ", Toast.LENGTH_SHORT).show();
                } else {
                    //register user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(nEmail,nPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                sendVerification();
                            }
                            else {
                                Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.alreadyUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(SignUp.this, MainActivity.class);
                startActivity(toLogin);
            }
        });

    }

    private void sendVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: Verification sent. Verify it and then login");
                    Toast.makeText(getApplicationContext(), "Verification sent. Verify it and then login", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Failed to send verification", Toast.LENGTH_SHORT).show();
        }

    }
}