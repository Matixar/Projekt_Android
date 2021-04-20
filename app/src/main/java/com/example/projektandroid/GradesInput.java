package com.example.projektandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class GradesInput extends AppCompatActivity {

    ArrayList<Grade> gradesList = new ArrayList<Grade>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_input);

        //pobranie ilości ocen z poprzedniej aktywności
        Bundle bundleIn = getIntent().getExtras();
        final int numberOfGrades = bundleIn.getInt("numberOfGrades");
        for(int i=0;i< numberOfGrades;i++)  //wygenerowanie ocen
        {
            Grade grade = new Grade(i+1);
            gradesList.add(grade);
        }
        //stworzenie adaptera dla ListView
        final GradesAdapter adapter = new GradesAdapter(this,gradesList);
        ListView gradesList = (ListView) findViewById(R.id.gradesList);
        gradesList.setAdapter(adapter);
        //obsługa przycisku do policzenia średniej
        Button averageButton = findViewById(R.id.averageButton);
        averageButton.setOnClickListener(view -> { computeAverage(); });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {  //w przypadku obrócenia ekranu zapisujemy oceny
        float gradeTab[] = new float[gradesList.size()];
        for(int i=0;i<gradesList.size();i++)
        {
            gradeTab[i]=gradesList.get(i).getValue();
        }
        outState.putFloatArray("grades",gradeTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) { //po obróceniu ekranu przywracamy wpisane wartości
        super.onRestoreInstanceState(savedInstanceState);
        float gradeTab[] = savedInstanceState.getFloatArray("grades");
        for(int i=0;i<gradesList.size();i++)
        {
            gradesList.get(i).setValue(gradeTab[i]);
        }

        final GradesAdapter adapter = new GradesAdapter(this,gradesList);
        ListView gradesList = (ListView) findViewById(R.id.gradesList);
        gradesList.setAdapter(adapter);
    }

    void computeAverage() { //funkcja obliczająca średnią i wysyła wynik do następnej aktywności kończąc bieżącą
        float sum = 0;
        float average = 0;
        int gradesNumber = gradesList.size();   //liczba wpisanych ocen - nie trzeba wpisywać wszystkich ocen żeby obliczyć średnią
        Bundle bundleOut = new Bundle();
        for (int i=0;i<gradesList.size();i++) {
            if(gradesList.get(i).getValue() == 0 )
                --gradesNumber; //jeżeli nie została wybrana ocena to zmniejszamy liczbę wpisanych ocen
            else
                sum += gradesList.get(i).getValue();
        }
        average = sum/(float)gradesNumber;
        //spakowanie średniej do bundle i wysłanie za pomocą intent
        bundleOut.putFloat("average",average);
        Intent intentOut = new Intent();
        intentOut.putExtras(bundleOut);
        setResult(RESULT_OK,intentOut);

        finish();
    }
}

