package com.example.notesapp;

import android.widget.TextView;

public class FirebaseModel {
    String title,content;

    public FirebaseModel() {
    }

    public FirebaseModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
