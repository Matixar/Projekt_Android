package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CarsInput extends AppCompatActivity {

    private EditText brand;
    private EditText model;
    private EditText fuelType;
    private EditText www;
    private String operationType;
    long id;

    Provider dbProvider;
    HelperDB helperDB;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_input);

        brand = (EditText) findViewById(R.id.editTextBrand);
        model = (EditText) findViewById(R.id.editTextModel);
        fuelType = (EditText) findViewById(R.id.editTextFuelType);
        www = (EditText) findViewById(R.id.editTextWWW);

        helperDB = new HelperDB(this);
        db = helperDB.getWritableDatabase();

        getOperationType();
    }

    public void getOperationType() {
        Bundle bundle = getIntent().getExtras();
        operationType = bundle.getString("operationType");
        if(operationType.contains("insert"))
            getSupportActionBar().setTitle("Dodaj pojazd do bazy");
        if(operationType.contains("update")) {
            getSupportActionBar().setTitle("Edytuj rekord");
            id = bundle.getLong("id");

            brand.setText(getRecordValue(id,"brand"));
            model.setText(getRecordValue(id,"model"));
            fuelType.setText(getRecordValue(id,"fuelType"));
            www.setText(getRecordValue(id,"www"));
        }
    }

    public boolean validate()
    {
        if(brand.getText().toString().length() >= 2 &&
        model.getText().toString().length() >= 2 &&
        fuelType.getText().toString().length() >= 3 &&
        www.getText().toString().contains(".") &&
        www.getText().toString().length() >= 5)
            return true;
        else return false;
    }

    public void save (View view) {
        if(validate()) {
            Intent intent = new Intent();
            intent.putExtra("operationType",operationType);
            setResult(RESULT_OK,intent);
            if(operationType.contains("insert")) addDBEntry();
            if(operationType.contains("update")) updateDBEntry();
            finish();
        }
        else Toast.makeText(CarsInput.this,"Niepoprawne dane",Toast.LENGTH_SHORT).show();
    }

    public void cancel (View view) {
        Toast.makeText(CarsInput.this,"Anulowano",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void www(View view) {
        if(www.getText().toString().contains(".") && www.getText().toString().length() >= 5) {
            Uri webpage;
            if(!www.getText().toString().startsWith("http://"))
                webpage = Uri.parse("http://" + www.getText().toString());
            else webpage = Uri.parse(www.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
            startActivity(intent);
        } else Toast.makeText(CarsInput.this,"Wpisz prawidłowy adres www",Toast.LENGTH_SHORT).show();
    }

    public String getRecordValue(long id, String column) {
        Cursor cursor = db.query(true,
                HelperDB.TABLE_NAME,
                new String[]{HelperDB.ID, HelperDB.COLUMN1, HelperDB.COLUMN2, HelperDB.COLUMN3, HelperDB.COLUMN4},
                HelperDB.ID + " = " + Long.toString(id),
                null,
                null,
                null,
                null,
                null);
        startManagingCursor(cursor);
        cursor.moveToFirst();

        String value = "";
        int index = cursor.getColumnIndexOrThrow(HelperDB.COLUMN1);

        while(!cursor.isAfterLast())
        {
            switch(column) {
                case "brand":
                    index = cursor.getColumnIndexOrThrow(HelperDB.COLUMN1);
                    break;
                case "model":
                    index = cursor.getColumnIndexOrThrow(HelperDB.COLUMN2);
                    break;
                case "fuelType":
                    index = cursor.getColumnIndexOrThrow(HelperDB.COLUMN3);
                    break;
                case "www":
                    index = cursor.getColumnIndexOrThrow(HelperDB.COLUMN4);
                    break;

            }

            value = cursor.getString(index);
            cursor.moveToNext();
        }
        return value;
    }

    private void addDBEntry() {
        ContentValues values = new ContentValues();
        dbProvider = new Provider();
        EditText brand = (EditText)
                findViewById(R.id.editTextBrand);
        EditText model = (EditText)
                findViewById(R.id.editTextModel);
        EditText www = (EditText)
                findViewById(R.id.editTextWWW);
        EditText fuelType = (EditText)
                findViewById(R.id.editTextFuelType);
        values.put(HelperDB.COLUMN1,brand.getText().toString());
        values.put(HelperDB.COLUMN2,model.getText().toString());
        values.put(HelperDB.COLUMN3,fuelType.getText().toString());
        values.put(HelperDB.COLUMN4,www.getText().toString());

        getContentResolver().insert(dbProvider.URI_CONTENT, values);
        finish();
    }

    private void updateDBEntry() {
        ContentValues values = new ContentValues();
        dbProvider = new Provider();

        values.put(HelperDB.COLUMN1,brand.getText().toString());
        values.put(HelperDB.COLUMN2,model.getText().toString());
        values.put(HelperDB.COLUMN3,fuelType.getText().toString());
        values.put(HelperDB.COLUMN4,www.getText().toString());

        getContentResolver().update(dbProvider.URI_CONTENT, values, HelperDB.ID + " = " + Long.toString(id),null);
        finish();
    }


}