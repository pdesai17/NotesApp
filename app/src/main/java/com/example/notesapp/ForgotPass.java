package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPass extends AppCompatActivity {
    ActivityForgotPassBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgotPassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        binding.newPassCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fEmail=binding.fEmailTIE.getText().toString();
                if(fEmail.isEmpty())
                {
                    Toast.makeText(ForgotPass.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }
                else {
                    //recover new password in firebase
                    firebaseAuth.sendPasswordResetEmail(fEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Reset your password. Link is sent to your mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPass.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Email is wrong or Account doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.goBackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin =new Intent(ForgotPass.this,MainActivity.class);
                startActivity(toLogin);
            }
        });
    }
}