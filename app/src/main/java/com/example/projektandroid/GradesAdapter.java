package com.example.projektandroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class GradesAdapter extends ArrayAdapter<Grade> {

    private List<Grade> gradeList;  //lista ocen
    private Activity context;   //kontekst
    float gradeSum = 0;

    public GradesAdapter(Activity context, List<Grade> gradeList) //konstruktor
    {
        super(context,R.layout.item_grade_layout,gradeList);

        this.gradeList = gradeList;
        this.context = context;
    }

    @Override
    public View getView(final int index, View viewForRecycle, ViewGroup parent)
    {
        View view = null;
        //tworzenie nowego wiersza
        if(viewForRecycle == null) {
            //tworzenie na podstawie pliku XML
            LayoutInflater pump = this.context.getLayoutInflater();
            view = pump.inflate(R.layout.item_grade_layout, null);
            view.setVisibility(View.VISIBLE);

            Spinner gradeSpinner = (Spinner) view.findViewById(R.id.gradeSelectSpinner);
            final View finalView = view;

            //ustawianie adaptera dla spinnera
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,R.array.grades,android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gradeSpinner.setAdapter(spinnerAdapter);
            gradeSpinner.setOnItemSelectedListener( //ustawienie listenera dla spinnera
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Grade element = (Grade) parent.getTag();
                            if(parent.getSelectedItem().toString().length() != 0)   //sprawdzenie czy wartość nie jest pusta
                                element.setValue(Float.parseFloat(parent.getSelectedItem().toString()));    //zapisanie wartości do obiektu Grade
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    }
            );
            gradeSpinner.setTag(gradeList.get(index));
        }
        else {  //aktualizacja istniejącego wiersza
            view = viewForRecycle;
            Spinner gradeSpinner = (Spinner) view.findViewById(R.id.gradeSelectSpinner);
            gradeSpinner.setTag(gradeList.get(index));
            Grade element = (Grade) gradeSpinner.getTag();
        }
        //ustawienie tekstu na podstawie modelu
        TextView gradeNumberText = (TextView) view.findViewById(R.id.gradeNumberText);
        gradeNumberText.setText(gradeList.get(index).getName());

        return view;    //zwrócenie nowego lub zaktualizowanego wiersza listy

    }

    //rozwiązanie problemu z znikaniem wybranych wartości podczas scrollowania
    @Override
    public int getViewTypeCount() { //ustawienie wielkości na podstawie ilości ocen
        return gradeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
