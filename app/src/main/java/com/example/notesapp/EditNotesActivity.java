package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivityEditNotesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNotesActivity extends AppCompatActivity {
    ActivityEditNotesBinding binding;
    String title,content,docId;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent data = getIntent();
        title = data.getStringExtra("title");
        content = data.getStringExtra("content");
        docId = data.getStringExtra("docId");
        binding.createTitleET.setText(title);
        binding.contentET.setText(content);

        setSupportActionBar(binding.titleToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.saveBtn.setOnClickListener(view -> {
            String newTitle = binding.createTitleET.getText().toString();
            String newContent = binding.contentET.getText().toString();
            DocumentReference documentReference = firebaseFirestore.collection("Notes")
                    .document(firebaseUser.getUid()).collection("MyNotes")
                    .document(docId);
            Map<String, String> map = new HashMap<>();
            map.put("title", newTitle);
            map.put("content", newContent);
            documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditNotesActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent toNotesActivity = new Intent(getApplicationContext(), NotesActivity.class);
                    startActivity(toNotesActivity);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditNotesActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }
}