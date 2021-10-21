package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivityCreateNotesBinding;
import com.example.notesapp.databinding.ActivityForgotPassBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNotesActivity extends AppCompatActivity {
    ActivityCreateNotesBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCreateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.titleToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=binding.createTitleET.getText().toString();
                String content=binding.contentTV.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(CreateNotesActivity.this, "Enter title and content", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference=firebaseFirestore
                            .collection("Notes")
                            .document(firebaseUser.getUid())
                            .collection("MyNotes")
                            .document();
                    Map<String,String> map=new HashMap<>();
                    map.put("title",title);
                    map.put("content",content);

                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateNotesActivity.this, "Notes Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent toNotesActivity=new Intent(getApplicationContext(),NotesActivity.class);
                            startActivity(toNotesActivity);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNotesActivity.this, "Fail to create note", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return false;
    }


}