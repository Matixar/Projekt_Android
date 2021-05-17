package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Functionality3 extends AppCompatActivity {

    private DrawView drawView;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality3);

        constraintLayout = findViewById(R.id.consLayout);
        linearLayout = new LinearLayout(this);

        drawView = new DrawView(this);
        constraintLayout.addView(drawView);
        addButtons();
    }

    public void addButtons() {
        linearLayout = new LinearLayout(this);

        // uniwersalne parametry dla przycisków - width,height,weight
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );

        // tworzenie przycisków
        Button buttonRed = new Button(this);
        Button buttonBlue = new Button(this);
        Button buttonYellow = new Button(this);
        Button buttonGreen = new Button(this);
        Button buttonClear = new Button(this);

        // ustawienie parametrów przycisków
        buttonRed.setLayoutParams(param);
        buttonBlue.setLayoutParams(param);
        buttonYellow.setLayoutParams(param);
        buttonGreen.setLayoutParams(param);
        buttonClear.setLayoutParams(param);

        // ustawienie kolorów/tesktu przycisków
        buttonRed.setBackgroundColor(Color.RED);
        buttonBlue.setBackgroundColor(Color.BLUE);
        buttonYellow.setBackgroundColor(Color.YELLOW);
        buttonGreen.setBackgroundColor(Color.GREEN);
        buttonClear.setText("X");


        // ustawienie listenerów na przyciskach
        buttonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setPaintColor("red"); // zmiana koloru paint w obiekcie DrawView
            }
        });

        buttonBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setPaintColor("blue");
            }
        });

        buttonYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setPaintColor("yellow");
            }
        });

        buttonGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setPaintColor("green");
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearScreen();
            }
        });

        // dodanie przycisków do layoutu
        linearLayout.addView(buttonRed);
        linearLayout.addView(buttonBlue);
        linearLayout.addView(buttonYellow);
        linearLayout.addView(buttonGreen);
        linearLayout.addView(buttonClear);

        // zagnieżdżenie (dodanie) layoutu z przyciskami w layoucie głównym
        constraintLayout.addView(linearLayout);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // zapisz obecną bitmapę
        outState.putParcelable("bitmap", drawView.getmBitmap());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Bitmap bmp = savedInstanceState.getParcelable("bitmap");

        // ustaw zapisaną bitmapę w obiekcie drawView
        drawView.setmBitmap(bmp);

        // ustaw zmienną logiczną drawView dotyczącą przywracania bitmapy po obrocie
        drawView.isTurned = true;
        super.onRestoreInstanceState(savedInstanceState);
    }
}