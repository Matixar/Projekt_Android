package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Functionality1 extends AppCompatActivity {
    //flagi sprawdzające czy użytkownik nacisnął dane pole tekstowe
    boolean nameSelected = false;
    boolean surnameSelected = false;
    boolean numberSelected = false;
    //flagi sprawdzające czy dane są poprawnie wpisane
    boolean nameReady = false;
    boolean surnameReady = false;
    boolean numberReady = false;
    //średnia
    float average = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality1);

        EditText name = (EditText) findViewById(R.id.editTextName);
        EditText surname = (EditText) findViewById(R.id.editTextSurname);
        EditText number = (EditText) findViewById(R.id.editTextNumber);
        validate(name, surname, number);    //inicjalizacja walidacji pól tekstowych
        //ustawienie listenerów przy zmianie focusu pól tekstowych
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    nameSelected = true;
                    validateFields(name, surname, number);
                    areAllFieldsFilled(name, surname, number);
                }
            }
        });
        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    surnameSelected = true;
                    validateFields(name, surname, number);
                    areAllFieldsFilled(name, surname, number);
                }
            }
        });
        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    numberSelected = true;
                    validateFields(name, surname, number);
                    areAllFieldsFilled(name, surname, number);
                }

            }
        });


    }
    protected void onActivityResult(int kodZadania, int resultCode, Intent data) {  //wyświetlenie średniej i modyfikacja przycisku
        super.onActivityResult(kodZadania,resultCode,data);
        if(resultCode == RESULT_OK) {
            //pobranie średniej z poprzedniej aktywności i wyświetlenie jej
            Bundle bundle = data.getExtras();
            this.average = bundle.getFloat("average");
            TextView avgGradeText = (TextView) findViewById(R.id.averageText);
            avgGradeText.setText("Twoja średnia to: "+average);
            avgGradeText.setVisibility(View.VISIBLE);
            Button button = (Button) findViewById(R.id.buttonGrades);
            //zablokowanie pól tekstowych
            findViewById(R.id.editTextName).setEnabled(false);
            findViewById(R.id.editTextSurname).setEnabled(false);
            findViewById(R.id.editTextNumber).setEnabled(false);
            //sprawdzenie czy uzyskano zaliczenie
            if(average >= 3.0)
            {
                button.setText("Super :)");
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                Toast.makeText(Functionality1.this,"Gratulacje! Otrzymujesz zaliczenie!",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
            else {
                button.setText("Tym razem mi nie poszło");
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                Toast.makeText(Functionality1.this,"Wysyłam podanie o zaliczenie warunkowe.",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //zachowaj średnią
        outState.putFloat("average", this.average);
        super.onSaveInstanceState(outState);
    }

    public void openGradesInput(View view) {    //obsługa przycisku do wpisywania ocen

        EditText name = (EditText) findViewById(R.id.editTextName);
        EditText surname = (EditText) findViewById(R.id.editTextSurname);
        EditText number = (EditText) findViewById(R.id.editTextNumber);

        int numberInt = Integer.parseInt(number.getText().toString());
        Intent intent = new Intent(this, GradesInput.class);
        Bundle bundle = new Bundle();
        //przekazanie wartości do bundle i wysłanie do aktywności docelowej za pomocą intent
        bundle.putString("name", name.getText().toString());
        bundle.putString("surname", surname.getText().toString());
        bundle.putInt("numberOfGrades", numberInt);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);

    }

    public boolean isEditTextNull(EditText field) { //sprawdzamy czy pole tekstowe jest puste
        return field.getText().toString().trim().length() == 0;
    }

    public void validateFields(EditText name, EditText surname, EditText number) {  //walidacja pól tekstowych
        //inicjalizacja tekstów błędów pól tekstowych
        TextView nameAlert = (TextView) findViewById(R.id.nameAlertTextView);
        TextView surnameAlert = (TextView) findViewById(R.id.surnameAlertTextView);
        TextView numberAlert = (TextView) findViewById(R.id.numberAlertTextView);

        if (isEditTextNull(name) && nameSelected) { //sprawdzenie czy pole imię jest puste
            Toast.makeText(Functionality1.this, "Wprowadź imię", Toast.LENGTH_SHORT).show();
            nameAlert.setText("Wprowadź imię");
            nameAlert.setVisibility(View.VISIBLE);
            nameReady=false;
        } else if(name.getText().toString().matches("[A-Za-z]{2,}+")){  //sprawdzenie czy imię jest dobrze zapisane (min. 3 znaki)
            nameAlert.setVisibility(View.GONE);
            nameReady = true;
        }
        else
        {   //imię nie jest poprawnie zapisane
            nameAlert.setText("Niepoprawne imię");
            nameAlert.setVisibility(View.VISIBLE);
            nameReady=false;
        }
        if (isEditTextNull(surname) && surnameSelected) {   //sprawdzenie czy pole nazwisko jest puste
            surnameAlert.setText("Wprowadź\n nazwisko");
            surnameAlert.setVisibility(View.VISIBLE);
            Toast.makeText(Functionality1.this, "Wprowadź nazwisko", Toast.LENGTH_SHORT).show();
            surnameReady=false;
        } else if(surname.getText().toString().matches("[A-Za-z-]{2,}+")){   //sprawdzenie czy nazwisko jest zapisane poprawnie
            surnameAlert.setVisibility(View.GONE);
            surnameReady = true;
        }
        else {  //niepoprawnie wpisane nazwisko
            surnameAlert.setText("Niepoprawne \nnazwisko");
            surnameAlert.setVisibility(View.VISIBLE);
            surnameReady=false;
        }
        if (number.getText().toString().isEmpty() && numberSelected) {  //sprawdzenie czy pole z liczbą ocen jest puste
            numberAlert.setText("Wprowadź liczbę\n ocen w zakresie \n[5,15]");
            numberAlert.setVisibility(View.VISIBLE);
            Toast.makeText(Functionality1.this, "Wprowadź liczbę ocen", Toast.LENGTH_SHORT).show();
        } else if(numberReady){ //sprawdzenie czy wpisano poprawną liczbę (jest to sprawdzane w funkcji areAllFieldsFilled)
            numberAlert.setVisibility(View.GONE);
        }
        else {  //wprowadzona jest liczba spoza zakresu
            numberAlert.setText("Wprowadź liczbę\n ocen w zakresie \n[5,15]");
            numberAlert.setVisibility(View.VISIBLE);
        }

    }

    public void areAllFieldsFilled(EditText name, EditText surname, EditText number) {
        Button grades = (Button) findViewById(R.id.buttonGrades);
        TextView gradesText = (TextView) findViewById(R.id.numberAlertTextView);
        if (!isEditTextNull(name) && !isEditTextNull(surname) && !isEditTextNull(number)) {

            int num = Integer.parseInt(number.getText().toString());
            if (num > 15 || num < 5) {
                numberReady = false;
                gradesText.setText("Liczba ocen\n w przedziale\n [5,15]");
                gradesText.setVisibility(View.VISIBLE);
                grades.setVisibility(View.INVISIBLE);
                Toast.makeText(Functionality1.this, "Liczba ocen musi być w przedziale [5,15]", Toast.LENGTH_SHORT).show();
            } else {
                numberReady = true;


                if(nameReady && surnameReady && numberReady)
                grades.setVisibility(View.VISIBLE);
            }
        } else {
            numberReady=false;
            grades.setVisibility(View.INVISIBLE);
        }
    }


    public void validate(EditText name, EditText surname, EditText number) {
        final EditText tab[] = {name,surname,number};   //tablica pól tekstowych
        for (final EditText element : tab) {
            element.addTextChangedListener( //ustawianie walidacji podczas edycji pól tekstowych
                    new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            validateFields(name, surname, number);
                            areAllFieldsFilled(name, surname, number);
                        }
                    }
            );
        }
    }

}