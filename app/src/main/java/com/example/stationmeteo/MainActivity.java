package com.example.stationmeteo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {
    DatabaseReference mydb;
    TextView temp, hum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);

        mydb = FirebaseDatabase.getInstance().getReference().child("dht11");

        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot temperatureSnapshot = dataSnapshot.child("temperature");
                DataSnapshot humiditySnapshot = dataSnapshot.child("humidity");

                String tempdata = temperatureSnapshot.getValue().toString();
                String humdata = humiditySnapshot.getValue().toString();
                temp.setText(tempdata);
                hum.setText(humdata);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}
