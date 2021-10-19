package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.notesapp.databinding.ActivityEditNotesBinding;

public class EditNotesActivity extends AppCompatActivity {
    ActivityEditNotesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}