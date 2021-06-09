package com.example.projektandroid;

import android.app.Activity;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

public class GetFileInfo extends AsyncTask<String, String, String> {

    private int fileSize;
    private String fileType;
    private Functionality4 activityFunctionality;

    /**
     * Konstruktor powiązujący obiekt klasy z główną aktywnością
     * @param activity
     */
    public GetFileInfo(Functionality4 activity) {
        this.activityFunctionality = activity;
        fileSize = 0;
        fileType = null;
        this.activityFunctionality.setFileSizeLabel("");
        this.activityFunctionality.setFileTypeLabel("");
    }

    /**
     * Metoda wykonująca się podczas działania wątku w tle aplikacji
     * @param strings
     * @return
     */
    @Override
    protected String doInBackground(String... strings) {

        String www = strings[0];

        HttpURLConnection connection = null;

        try {
            // utwórz połączenie http z plikiem
            URL url = new URL(www);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // pobierz rozmiar i typ pliku
            fileSize = connection.getContentLength();
            fileType = connection.getContentType();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect(); // zamknij połączenie
        }

        return "";

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Metoda wywoływana po zakończeniu wykonywania działania
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // jeżeli rozmiar pliku > 0 (uzyskano do niego dostęp)
        if (fileSize > 0) {
            // ustaw odpowiednie wartości (rozmiar, typ pliku) w etykietach głównej aktywności
            this.activityFunctionality.setFileSizeLabel(Integer.toString(fileSize));
            this.activityFunctionality.setFileTypeLabel(fileType);
        } else {
            // w przeciwnym wypadku - umieść w etykietach informację o niepowodzeniu się próby dostępu do pliku
            this.activityFunctionality.setFileSizeLabel("Could not access\nspecified file");
            this.activityFunctionality.setFileTypeLabel("Could not access\nspecified file");
        }


    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }


}
