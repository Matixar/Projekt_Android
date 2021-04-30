package com.example.projektandroid;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {

    private HelperDB helperDB;

    private static final String IDENTIFIER = "com.example.projektandroid.Provider";
    public static final Uri URI_CONTENT = Uri.parse("content://" + IDENTIFIER + "/" + HelperDB.TABLE_NAME);

    private static final int WHOLE_TABLE = 1;
    private static final int SELECTED_ROW = 2;

    private static final UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatch.addURI(IDENTIFIER,HelperDB.TABLE_NAME,WHOLE_TABLE);
        uriMatch.addURI(IDENTIFIER,HelperDB.TABLE_NAME +"/#",SELECTED_ROW);
    }


    @Override
    public boolean onCreate() {
        helperDB = new HelperDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int uriType = uriMatch.match(uri);
        helperDB = new HelperDB(getContext());
        SQLiteDatabase db = helperDB.getWritableDatabase();

        Cursor cursor = null;

        switch (uriType) {
            case WHOLE_TABLE:
            case SELECTED_ROW:
                cursor = db.query(true,
                        HelperDB.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = uriMatch.match(uri);
        helperDB = new HelperDB(getContext());
        SQLiteDatabase db = helperDB.getWritableDatabase();

        long idAdded = 0;
        switch (uriType)
        {
            case WHOLE_TABLE:
                idAdded = db.insert(HelperDB.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse(HelperDB.TABLE_NAME + "/" + idAdded);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatch.match(uri);
        helperDB = new HelperDB(getContext());
        SQLiteDatabase db = helperDB.getWritableDatabase();

        int deletedCount = 0;
        switch(uriType){
            case WHOLE_TABLE:
            case SELECTED_ROW:
                deletedCount = db.delete(HelperDB.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatch.match(uri);
        helperDB = new HelperDB(getContext());
        SQLiteDatabase db = helperDB.getWritableDatabase();

        int updatedCount = 0;
        switch (uriType) {
            case WHOLE_TABLE:
            case SELECTED_ROW:
                updatedCount = db.update(HelperDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return updatedCount;
    }
}
