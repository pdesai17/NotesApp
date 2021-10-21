package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notesapp.databinding.ActivityNoteDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.$Gson$Preconditions;

import static android.app.PendingIntent.getActivity;

public class NoteDetail extends AppCompatActivity {
    ActivityNoteDetailBinding binding;
    private static String TAG="NoteDetail";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String title,content,docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.titleToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        Intent data=getIntent();
        title=data.getStringExtra("title");
        content=data.getStringExtra("content");
        docId=data.getStringExtra("docId");
        binding.createTitleET.setText(title);
        binding.contentTV.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.editNote:
                Intent toEdit=new Intent(this,EditNotesActivity.class);
                toEdit.putExtra("title",title);
                toEdit.putExtra("content",content);
                toEdit.putExtra("docId",docId);
                startActivity(toEdit);
                finish();
                break;
            case R.id.deleteNote:
                deleteNote();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_ic_menu,menu);
        return true;
    }

    private void deleteNote() {
        DocumentReference documentReference=firebaseFirestore
                .collection("Notes")
                .document(firebaseUser.getUid())
                .collection("MyNotes")
                .document(docId);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NoteDetail.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteDetail.this, "Failed to delete", Toast.LENGTH_SHORT).show();

            }
        });
    }




}