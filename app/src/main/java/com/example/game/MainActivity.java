package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BeforeGame beforeGame = new BeforeGame(this);
        setContentView(beforeGame);


    }
}