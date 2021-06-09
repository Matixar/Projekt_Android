package com.example.projektandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.system.ErrnoException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader extends IntentService {

    private int bytesDownloaded;
    public final static String RECEIVER = "com.example.intent_service.receiver";
    public final static String INFO = "info";

    private DownloadProgress progress;
    private static final String ACTION_DOWNLOAD = "com.example.intent_service.action.download";
    private static final String PARAMETER1 =
            "com.example.intent_service.extra.url";


    public FileDownloader() {
        super("FileDownloader");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FileDownloader(String name) {
        super(name);
    }

    /**
     * Metoda służąca do uruchamiania usługi.
     *
     * @param context
     * @param parameter
     */
    public static void runService(Context context, String parameter) {
        Intent intent = new Intent(context, FileDownloader.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(PARAMETER1, parameter);
        context.startService(intent);
    }

    /**
     * Metoda obsługująca otrzymany intent - jeżeli jest on poprawny
     * (niepusty i zawiera właściwość action odpowiednią intentom tworzonym przez metodę runService) -
     * wywoływana jest metoda execute
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String url = intent.getStringExtra(PARAMETER1);

                execute(url);
            } else {
                Log.e("intent_service", "unknown action");
            }
        }
        Log.d("intent_service", "intent executed successfully");
    }

    /**
     * Metoda wykonująca pobieranie pliku z serwera
     *
     * @param strUrl
     */
    private void execute(String strUrl) {
        FileOutputStream outStream = null;
        HttpURLConnection connection = null;
        InputStream inStream = null;
        File outFile = null;
        progress = new DownloadProgress();

        bytesDownloaded = 0;
        try {
            // utwórz połączenie htpp z plikiem na podstawie otrzymanego url
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setDoOutput(true);

            // utwórz plik, do którego zapisany zostanie pobierany plik
            File tempFile = new File(url.getPath());
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if(!directory.exists())
                directory.mkdirs();
            outFile = new File(
                    directory,tempFile.getName());
            if (outFile.exists()) outFile.delete();


            // utwórz czytnik danych z połączenia http
            DataInputStream reader = new DataInputStream(connection.getInputStream());
            // utwórz strumień zapisu odczytywanych danych do utworzonego wcześniej pliku

            outStream = new FileOutputStream(outFile.getPath());

            // rozmiar bloku pobieranych danych
            int BLOCK_SIZE = 1024 * 1024 * 5;
            byte buffer[] = new byte[BLOCK_SIZE];

            progress.size = connection.getContentLength();
            progress.finished = 0;

            int downloaded = reader.read(buffer, 0, BLOCK_SIZE);
            outStream.flush();
            // dopóki pobierane są dane
            while (downloaded != -1) {
                // zapisz pobrany blok danych do pliku
                outStream.write(buffer, 0, downloaded);
                bytesDownloaded += downloaded;
                progress.bytesDownloaded = bytesDownloaded;
                // wyślij transmisję z danymi nt. pobierania
                transmitBroadcast(progress);

                // odczytaj kolejny blok danych
                downloaded = reader.read(buffer, 0, BLOCK_SIZE);
                Log.d("downloading file:" + outFile.getName(), ": " + Integer.toString(downloaded) + " bytes");
            }
            progress.finished = 1;
            transmitBroadcast(progress);
            Log.d("downloaded file: " + outFile.getPath() + outFile.getName(), ": " + Integer.toString(bytesDownloaded) + " bytes");

        } catch (Exception e) {
            Log.e("operation_permission", e.getCause()+", "+e.getMessage());
        }
        // pozamykaj strumienie i połączenia
        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) connection.disconnect();
            MediaScannerConnection.scanFile(
                    this,
                    new String[]{ outFile.getAbsolutePath() }, // "file" was created with "new File(...)"
                    null,
                    null);
        }

    }

    /**
     * Metoda służąca do przesyłania (transmitowania) danych nt. pobierania zawartych w obiekcie klasy DownloadProgress
     *
     * @param progress
     */
    public void transmitBroadcast(DownloadProgress progress) {
        Intent intent = new Intent(RECEIVER);
        intent.putExtra(INFO, progress);
        sendBroadcast(intent);
    }


}
