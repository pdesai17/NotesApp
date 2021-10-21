package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SpannableString spannableStr = new SpannableString("New User? Sign Up");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        spannableStr.setSpan(foregroundColorSpan, 10, 17, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.signUpTV.setText(spannableStr);


        firebaseAuth = FirebaseAuth.getInstance();
        binding.signUpCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.passTIL.setError(null);
                binding.emailTIL.setError(null);
                if (validate()) {
                    firebaseAuth.signInWithEmailAndPassword(binding.emailTIE.getText().toString().trim(), binding.passTIE.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: success");
                                checkMailVerification();
                            } else {
                                Log.d(TAG, "onComplete: not success");
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }  //register user to firebase

            }

        });
        binding.forgotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toForgotPass = new Intent(MainActivity.this, ForgotPass.class);
                startActivity(toForgotPass);
            }
        });
        binding.signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(toSignUp);
            }
        });
    }

    private void checkMailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()) {
            Log.d(TAG, "checkMailVerification: verified");
            Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            Log.d(TAG, "checkMailVerification: not verified");
            Toast.makeText(getApplicationContext(), "Verify first", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate() {
        if (binding.emailTIE.getText() != null && binding.emailTIE.getText().toString().isEmpty()) {
            binding.emailTIL.setError("Email must not be empty");
            return false;
        } else if (binding.passTIE.getText() != null && binding.passTIE.getText().toString().isEmpty()) {
            binding.passTIL.setError("Password must not be empty");
            return false;
        } else {
            return true;
        }
    }
}




   /* val db = Firebase.firestore;

    val table = db.collection("user")
    val query = table.whereEqualTo("isAdmin", true).get().addOnSuccessListener {documents ->
        for (document in documents) {
        Log.d(TAG, "onCreate: data "+document.get("name"))
        }
        }*/