package com.kelesit.travelbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> names = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.add_place){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("info", "new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        /*Cursor cursor = MapsActivity.db.rawQuery("SELECT name FROM places",null);
        int i = 0;


        while(cursor.moveToNext()){
            names.add(i,cursor.getString(i));
            i++;
        }*/

        try{
            MapsActivity.db = this.openOrCreateDatabase("Places", MODE_PRIVATE, null);
            Cursor cursor = MapsActivity.db.rawQuery("SELECT name FROM places",null);

            int nameIx = cursor.getColumnIndex("name");
            int latitudeIx = cursor.getColumnIndex("latitude");
            int longitudeIx = cursor.getColumnIndex("longitude");

            while (cursor.moveToNext()){

                String nameFromDb = cursor.getString(nameIx);
                String latitudeFromDb = cursor.getString(latitudeIx);
                String longitudeFromDb = cursor.getString(longitudeIx);

                names.add(nameFromDb);

                Double longi = Double.parseDouble(latitudeFromDb);
                Double lati = Double.parseDouble(longitudeFromDb);

                LatLng locationFromDb = new LatLng(longi, lati);
                locations.add(locationFromDb);

                System.out.println("name " + names.get(0));

            }

            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("info", "old");
                intent.putExtra("position", position);

                startActivity(intent);
            }
        });
    }
}
