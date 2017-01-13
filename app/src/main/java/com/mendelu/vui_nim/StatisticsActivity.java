package com.mendelu.vui_nim;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.mendelu.vui_nim.db.Archive;
import com.mendelu.vui_nim.db.ArchiveOpenHelper;
/**
 * Created by Jakub on 06.06.2016.
 *
 * Class is made for work with data from Archive.
 * It's a bridge between ArchiveOpenHelper and ShowActivity.
 */
public class StatisticsActivity extends ListActivity {

    ArchiveOpenHelper aoh;
    ShowActivity show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForContextMenu(getListView());

        aoh = new ArchiveOpenHelper(this);
        show = new ShowActivity();

        /**
         * Set Cursor c is for random read-write access to the result set returned by a database query.
         */
        Cursor c = aoh.getReadableDatabase().query(
                Archive.TABLE_NAME,
                new String[] {Archive.ID, Archive.NAME, Archive.RESULT, Archive.NUMBER, Archive.DATE},
                null,
                null,
                null,
                null,
                null);

        /**
         * CursorAdapter adapter exposes data from a Cursor c to a List Adapter.
         */
        CursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                c,
                new String[] {Archive.NAME,Archive.DATE},
                new int[] {android.R.id.text1, android.R.id.text2});

        setListAdapter(adapter);
    }

    /**
     * Created menu including a popup window for extension.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function manage ListView and it's adapter.
     * Creation of intent as parameters for another activity.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) l.getItemAtPosition(position);
        String idThis = "";
        String name = "";
        String result = "";
        String number = "";
        String date = "";
        if(cursor.moveToPosition(position)) {
            idThis = cursor.getString(cursor.getColumnIndex(Archive.ID));
            name = cursor.getString(cursor.getColumnIndex(Archive.NAME));
            result = cursor.getString(cursor.getColumnIndex(Archive.RESULT));
            number = cursor.getString(cursor.getColumnIndex(Archive.NUMBER));
            date = cursor.getString(cursor.getColumnIndex(Archive.DATE));
        }
        Intent i = new Intent(this, ShowActivity.class);
        i.putExtra("ID", idThis);
        i.putExtra("NAME",name);
        i.putExtra("RESULT",result);
        i.putExtra("NUMBER",number);
        i.putExtra("DATE",date);
        startActivity(i);
    }
}
