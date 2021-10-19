package com.example.notesapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notesapp.databinding.ActivityNotesBinding;
import com.example.notesapp.databinding.SingleNoteBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Random;

import static android.graphics.Color.parseColor;

public class NotesActivity extends AppCompatActivity {
    private static final String TAG = "Notes";
    private ActivityNotesBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirebaseModel,NotesViewHolder> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFBE0B")));
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();


       // FirebaseModel firebaseModel=new FirebaseModel();

        binding.addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this, CreateNotesActivity.class));
            }
        });



        Query query=firebaseFirestore.collection("Notes").document(firebaseUser.getUid()).collection("MyNotes").orderBy("title", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<FirebaseModel> allNotes=new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();
        notesAdapter=new FirestoreRecyclerAdapter<FirebaseModel, NotesViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull FirebaseModel model) {
                Log.d(TAG, "onBindViewHolder: title= "+model.getTitle());
                holder.titleTV.setText(model.getTitle());
                holder.contentTV.setText(model.getContent());
                holder.randomIV.setImageResource(setRandomImg());

                String docId=notesAdapter.getSnapshots().getSnapshot(position).getId();

                holder.notes.setOnClickListener(view -> {
                    Intent toNoteDetail=new Intent(getApplicationContext(), NoteDetail.class);
                    toNoteDetail.putExtra("title",model.getTitle());
                    toNoteDetail.putExtra("content",model.getContent());
                    toNoteDetail.putExtra("docId",docId);
                    startActivity(toNoteDetail);
                });
            }

            @NonNull
            @Override
            public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note,parent,false));
            }
        };

        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.notesRV.setHasFixedSize(true);
        binding.notesRV.setLayoutManager(staggeredGridLayoutManager);
        binding.notesRV.setAdapter(notesAdapter);


    }
    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView titleTV,contentTV;
        ImageView randomIV;
        CardView notes;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV=itemView.findViewById(R.id.titleTV);
            contentTV=itemView.findViewById(R.id.contentTV);
            randomIV=itemView.findViewById(R.id.randomIV);
            notes=itemView.findViewById(R.id.item_notes);
            notes.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            getMenuInflater().inflate(R.menu.notes_menu,contextMenu);
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.edit:
                Toast.makeText(this, "edit selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Toast.makeText(this, "deleted notes", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logOut:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (notesAdapter != null) {
            notesAdapter.stopListening();
        }
    }



    private int setRandomImg() {
        Integer random[]={R.drawable.ic_green, R.drawable.ic_blue, R.drawable.ic_red, R.drawable.ic_pink,R.drawable.ic_orange};
        Random randomNo=new Random();
        int random1=randomNo.nextInt(5);
        return random[random1];
    }
}