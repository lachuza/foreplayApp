package com.example.foreplayapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

    }

    /**
     * Called when the user taps the Send button
     */
    public void startGame(View view) {
        boolean start = false;
        Intent intent = new Intent(this, GameActivity.class);
        ConstraintLayout layout = findViewById(R.id.layout);
        intent.putExtra("HEIGHT", layout.getMeasuredHeight());
        intent.putExtra("WIDTH", layout.getMeasuredWidth());
        TextView playerMale = findViewById(R.id.namePlayerMale);
        TextView playerFemale = findViewById(R.id.namePlayerFemale);
        if(!playerMale.getText().toString().matches("")){
            intent.putExtra("MALE", playerMale.getText().toString());
            start = true;
        } else {
            start = false;
            alertDialog("Debe ingresar el nombre del jugador");
            return;
        }
        if(!playerFemale.getText().toString().matches("")){
            intent.putExtra("FEMALE", playerFemale.getText().toString());
            start = true;
        } else {
            start = false;
            alertDialog("Debe ingresar el nombre de la jugadora");
            return;
        }
        if (start) {
            startActivity(intent);
        }


    }


    private void alertDialog(String message) { // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        // add a button
        builder.setPositiveButton("OK", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
