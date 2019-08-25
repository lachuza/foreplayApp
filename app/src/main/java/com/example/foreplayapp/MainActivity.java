package com.example.foreplayapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /** Called when the user taps the Send button */
    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        ConstraintLayout layout = findViewById(R.id.layout);
        intent.putExtra("HEIGHT", layout.getMeasuredHeight());
        intent.putExtra("WIDTH", layout.getMeasuredWidth());
        startActivity(intent);
    }











}
