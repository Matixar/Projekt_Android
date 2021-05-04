package com.example.projektandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Functionality2 extends AppCompatActivity implements AbsListView.MultiChoiceModeListener, LoaderManager.LoaderCallbacks<Cursor> {
    private HelperDB helperDB;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView list;
    private SimpleCursorAdapter dbAdapter;
    private Provider dbProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality2);

        helperDB = new HelperDB(this);
        db = helperDB.getWritableDatabase();
        list = (ListView) findViewById(R.id.listViewDB);
        TextView emptyText = (TextView) findViewById(R.id.empty);
        list.setEmptyView(emptyText);
        setUpContextualMenu();
        setUpOnClickListener();
        
        initializeLoader();
    }

    private void initializeLoader() {
        getLoaderManager().initLoader(0,
                null,
                this);
        String[] mapFrom = new String[]{HelperDB.COLUMN1,HelperDB.COLUMN2};
        int[] mapTo = new int[]{R.id.textView_brand,R.id.textView_model};
        dbAdapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.item_list,cursor,mapFrom,mapTo);
        list.setAdapter(dbAdapter);
    }

    private void setUpOnClickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getApplicationContext(),CarsInput.class);
                intent.putExtra("operationType","update");
                intent.putExtra("id",id);

                startActivityForResult(intent, new Integer(0));
            }
        });
    }

    public void setUpContextualMenu() {
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int checkedCount = 0;

            private MenuItem deleteCounter;
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(checked) checkedCount++;
                    else checkedCount--;

                deleteCounter.setTitle(Integer.toString(checkedCount));
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                deleteCounter = menu.findItem(R.id.menu_counter);
                checkedCount = 0;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.deleteSmartphones:
                        deleteSelected();
                        checkedCount = 0;

                        deleteCounter.setTitle(Integer.toString(checkedCount));
                        Toast.makeText(Functionality2.this,"Usuwanie zaznaczonych elementów",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void deleteSelected() {
        long checked[] = list.getCheckedItemIds();
        for (int i = 0; i < checked.length; ++i) {
            getContentResolver().delete(ContentUris.withAppendedId(com.example.projektandroid.Provider.URI_CONTENT, checked[i]), HelperDB.ID + " = " + Long.toString(checked[i]), null);
        }
    }

    public void openAddCarActivity(View view) {
        Intent intent = new Intent (getApplicationContext(),CarsInput.class);
        intent.putExtra("operationType","insert");
        startActivityForResult(intent,new Integer(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String operationType = bundle.getString("operationType");
            if(operationType.startsWith("insert"))
            {
                Toast.makeText(Functionality2.this,"Nowy rekord dodany",Toast.LENGTH_SHORT).show();
            }
            if(operationType.startsWith("update"))
            {
                Toast.makeText(Functionality2.this,"Rekord zaktualizowany",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {HelperDB.ID, HelperDB.COLUMN1, HelperDB.COLUMN2};
        CursorLoader cLoader = new CursorLoader(this,
                com.example.projektandroid.Provider.URI_CONTENT, projection, null, null, null);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dbAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dbAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cars_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        if (item.getItemId() == R.id.addCar) {
            Intent intent = new Intent(this, CarsInput.class);
            intent.putExtra("operationType", "insert");
            startActivityForResult(intent, new Integer(0));
            return true;
        } else return super.onOptionsItemSelected(item);

    }

}