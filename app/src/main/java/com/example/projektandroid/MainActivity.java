package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openFunctionality1(View view) { //obsługa przycisku funkcjonalność 1
        Intent intent = new Intent(this,Functionality1.class);
        startActivity(intent);
    }

    public void openFunctionality2(View view) { //obsługa przycisku funkcjonalność 2
        Intent intent = new Intent(this,Functionality2.class);
        startActivity(intent);
    }

    public void openFunctionality3(View view) { //obsługa przycisku funkcjonalność 3
        Intent intent = new Intent(this,Functionality3.class);
        startActivity(intent);
    }
}